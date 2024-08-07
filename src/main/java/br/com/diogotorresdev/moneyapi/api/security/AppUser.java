package br.com.diogotorresdev.moneyapi.api.security;

import br.com.diogotorresdev.moneyapi.api.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUser extends User {
    private static final long serialVersionUID = 1L;

    private UserAccount userAccount;

    public AppUser(UserAccount userAccount, Collection<? extends GrantedAuthority> authorities) {
        super(userAccount.getEmail(), userAccount.getUserAccountPassword(), authorities);

        this.userAccount = userAccount;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }
}
