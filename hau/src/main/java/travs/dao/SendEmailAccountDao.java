package travs.dao;

import travs.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SendEmailAccountDao  extends JpaRepository<Account,Long> {


    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    Account findAccountByEmail(String email);

    Account findAccountByResetPasswordToken(String token);

}
