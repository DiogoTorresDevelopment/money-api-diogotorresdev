package br.com.diogotorresdev.moneyapi.api.repository;

import br.com.diogotorresdev.moneyapi.api.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    public Optional<UserAccount> findByEmail(String email);

    public List<UserAccount> findByPermissionsPermissionDescription(String permissionDescription);
}
