package travs.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import travs.entity.account.Account;

import java.util.List;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {

    Account getAccountByEmail(String email);

    Account getAccountById(String id);

    @Modifying
    @Query(value = "DELETE FROM Account a WHERE a.id=:id" , nativeQuery = true)
    void deleteAllById( @Param("id") String id);

    @Query(value = " SELECT count (a.*) FROM account a inner join Role r  on a.role_id = r.id WHERE  a.name LIKE CONCAT('%',:name,'%') AND a.email LIKE CONCAT('%',:email,'%')   AND r.name IN (:roles) ", nativeQuery = true)
    Long countAccountByNameAndEmailAndRole(@Param("name") String name, @Param("email") String email, @Param("roles") List<String> roles);


    @Query(value = "SELECT a.* FROM account a inner join Role r  on a.role_id = r.id WHERE  a.name LIKE CONCAT('%',:name,'%') AND a.email LIKE CONCAT('%',:email,'%')  AND r.name IN (:roles) order by created_at desc ", nativeQuery = true)
    Page<Account> searchAccountByNameEmailRole(@Param("name") String name, @Param("email") String email, @Param("roles") List<String> roles, Pageable pageable);



    @Query("SELECT a FROM Account  a WHERE a.id =?1 ")
    Account getAccountByIdAndRole(Long accountId);



}
