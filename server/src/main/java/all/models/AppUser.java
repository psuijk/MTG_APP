package all.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class AppUser extends User {
    private static final String AUTHORITY_PREFIX = "ROLE_";

    private int appUserId;
    private String firstName;
    private String lastName;
    //private final Role appUserRole;

    public AppUser(int appUserId, String username, String password,
                   boolean disabled, List<String> roles, String firstName, String lastName) {
        super(username, password, !disabled,
                true, true, true,
                convertRolesToAuthorities(roles));
        this.appUserId = appUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        //this.appUserRole = Role.findByName(roles.get(0));
    }

    private List<String> roles = new ArrayList<>();

    /*public Role getAppUserRole() {
        return appUserRole;
    }*/

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (String role : roles) {
            Assert.isTrue(!role.startsWith(AUTHORITY_PREFIX),
                    () ->
                            String.
                                    format("%s cannot start with %s (it is automatically added)",
                                            role, AUTHORITY_PREFIX));
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role));
        }
        return authorities;
    }

    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> a.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }
}