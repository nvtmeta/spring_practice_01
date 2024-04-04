package fsa.training.ems_springboot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return Optional.of(springSecurityUser.getUsername());
        }
        return Optional.empty();
    }

    //    check role
    public static List<String> getCurrentUserRoles() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replaceAll("ROLE_", ""))
                .toList();
    }

    public static boolean isAdmin() {
        return getCurrentUserRoles().contains("ADMIN");
    }
}
