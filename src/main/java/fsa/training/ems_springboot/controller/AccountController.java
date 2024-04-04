package fsa.training.ems_springboot.controller;


import fsa.training.ems_springboot.model.dto.AccountDetailDto;
import fsa.training.ems_springboot.model.entity.Account;
import fsa.training.ems_springboot.security.SecurityUtils;
import fsa.training.ems_springboot.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/my-profile")
    public String myProfile(Model model) {
//        get current user: SecurityUtils.getCurrentUser()
//        get account detiails : AccountRepository.findByUsername(username)
        // return the view with account details
        // set account to model attribute

        String username = SecurityUtils.getCurrentUserLogin().orElseThrow() ;
        System.out.println("username: " + username);
        Optional<Account> account = accountService.findByUsernameAndActivatedTrue(username);
        if (account.isEmpty()) {
            return "redirect:/";
        }
        AccountDetailDto accountDetailDto = new AccountDetailDto();
        BeanUtils.copyProperties(account.get(), accountDetailDto);
        model.addAttribute("accountDetailDto", accountDetailDto);
        return "account/detail";
    }
}
