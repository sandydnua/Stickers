package stickers.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import stickers.database.entity.Accaunt;
import stickers.services.DbServices;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private DbServices dbServices;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;
//
//    @Autowired
//    @Qualifier("databaseManager")
//    private DatabaseManager databaseManager;
//
//    @Autowired
//    @Qualifier("accessManager")
//    private AccessManager accessManager;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String startPage() {
         return "login";
    }

    @PostMapping("login")
    public String signin(@RequestParam(name = "email") String email,
                         @RequestParam(name = "password") String password,
                         HttpSession session) {
        Accaunt accaunt = dbServices.getAccaunt(email, password);
        if ( null != accaunt && accaunt.isConfirm()) {
            session.setAttribute("accaunt", accaunt);
            dbServices.setActivAccaunt(accaunt);
            String requestedPage = (String) session.getAttribute("requestedPage");
            if (requestedPage != null) {
                return "redirect:" + requestedPage;
            } else {
                return "redirect:mainpage";
            }
        } else {
            session.removeAttribute("accaunt");
            return "redirect:login";
        }
    }

    @GetMapping("signout")
    public String signout(HttpSession session) {
        session.removeAttribute("accaunt");
        session.removeAttribute("requestedPage");
        dbServices.clearActivAccaunt();
        return "redirect:login";
    }
    @GetMapping("/")
    public String mainPageDefault(HttpSession session) {
        return mainPage(session, null);
    }


    @GetMapping("mainpage")
    public String mainPage(HttpSession session, @RequestParam(name = "requestedBoardId", required = false) Integer requestedBoardId) {
        Accaunt accaunt = (Accaunt) session.getAttribute("accaunt");
        if (requestedBoardId != null ) {
            // если есть права
//            Set<String> allPermittedOperations = accessManager.getAllAccessesToBoardForCurrentAccaunt(requestedBoardId, accaunt);
//            if( accessManager.containRead(allPermittedOperations)) {
            if( dbServices.canCurrentUserReadBoard(requestedBoardId)) {
                // может просматривать как минимум
                session.setAttribute("requestedBoardId", requestedBoardId);
            }
            return "mainpage";
        } else {
            // TODO тут надо отдельно обрабатывать разные ошибки. и как-то подругому
            session.setAttribute("requestedPage", "mainpage");
            return (dbServices.getActivAccaunt() != null)? "mainpage" : "redirect:login";
        }
    }

    @GetMapping("registration")
    public String registration() {
        return "registration";
    }


    @PostMapping("registration")
    public String registration(@RequestParam(name = "email") String email,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "firstname") String firstname,
                               @RequestParam(name = "lastname") String lastname,
                               Model model) {
        if(dbServices.getAccaunt(email) != null) {
            //TODO
            System.out.println("Accaunt exists !!!");
        } else {
            Accaunt accaunt = dbServices.createAccaunt(new Accaunt(email, password, firstname, lastname));
            model.addAttribute("idNewAccaunt", accaunt.getId());
            //TODO тут надо отправить письмо со ссылкой для подтверждения аккаунта
            sendMailForConfirmAccaunt(accaunt.getId(), accaunt.getEmail());
            return "redirect:confirmPage";
        }

        return "redirect:mainpage";
    }

    @GetMapping("profile")
    public String profile(HttpSession session){
        session.setAttribute("requestedPage", "profile");
        return (session.getAttribute("accaunt") != null)? "profile" : "redirect:login";
    }

    @GetMapping("confirmPage")
    public String confirmPage() {
        return "confirmPage";
    }

    @GetMapping("confirmNewAccaunt")
    public String confirmNewAccaunt(@RequestParam(name = "id", required = false) Integer id) {

        dbServices.confirmAccaunt(id);

        return "redirect:login";
    }


    private void sendMailForConfirmAccaunt(Integer id, String email) {
        // Создаём потокобезопасную копию шаблона.
        SimpleMailMessage mailMessage = new SimpleMailMessage(templateMessage);

        //TODO: Сюда напишите свой e-mail получателя.
        mailMessage.setTo("sandy.dn.ua@gmail.com");

        //TODO сейчас не получается подменить адрес отправитель. отправляют через гугл, а он скорее всего не дает это делать. надо использовать другой сервер.
        mailMessage.setFrom("blabla@mail");

        //TODO шаброн письма надо хрань в файле
        mailMessage.setText("Для подтверждения аккаунта перейтиде по ссылке:\n http://localhost:8080/stickers/confirmNewAccaunt?id=" + id.toString());
        try {
            mailSender.send(mailMessage);
        } catch (MailException mailException) {
            System.out.println("Mail send failed.");
            mailException.printStackTrace();
        }
    }

}
