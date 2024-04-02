package fsa.training.ems_springboot;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPassword {

    public static void main(String[] args) {

        String rawPassword = "12346";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(encodedPassword);

        System.out.println(passwordEncoder.matches(rawPassword, "$2a$10$WmOPmbX.aqFzhahB/.bbB.iVK7lRujF1Rso.XPA6nzCpkiGHVV4Qm"));
        System.out.println(passwordEncoder.matches(rawPassword, "$2a$10$8FmDXi1KUG6.IPMnxk7nXO/QhSCXOwbFTser9sGf4oklSkMyMnoOO"));
    }
}
