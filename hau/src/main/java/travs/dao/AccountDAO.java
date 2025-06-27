package travs.dao;

import travs.entity.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {

    Account getAccountByEmail(String email);

    Account getAccountById(String id);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Account a WHERE a.id=:id" , nativeQuery = true)
    void deleteAllById( @Param("id") String id);

    @Query(value = " SELECT count (a.*) FROM account a inner join Role r  on a.role_id = r.id WHERE  a.name LIKE CONCAT('%',:name,'%') AND a.email LIKE CONCAT('%',:email,'%')   AND r.name IN (:roles) ", nativeQuery = true)
    Long countAccountByNameAndEmailAndRole(@Param("name") String name, @Param("email") String email, @Param("roles") List<String> roles);


    @Query(value = "SELECT a.* FROM account a inner join Role r  on a.role_id = r.id WHERE  a.name LIKE CONCAT('%',:name,'%') AND a.email LIKE CONCAT('%',:email,'%')  AND r.name IN (:roles) order by created_at desc ", nativeQuery = true)
    Page<Account> searchAccountByNameEmailRole(@Param("name") String name, @Param("email") String email, @Param("roles") List<String> roles, Pageable pageable);

//    @Query(" SELECT a FROM Account a WHERE a.name LIKE CONCAT('%',:name,'%') AND a.phone LIKE CONCAT('%',:phoneNumber,'%') AND a.identifyCard LIKE CONCAT('%',:identityCard,'%') AND a.role.name IN :roles ORDER BY a.id")
//    Page<Account> searchAccountByNamePhoneIdentifyCardAndRole(@Param("name") String name, @Param("phoneNumber")
//            String phoneNumber, @Param("identityCard") String identityCard, @Param("roles") String roles, Pageable pageable);

    @Query(value = "SELECT a.* FROM account a inner join Role r  on a.role_id = r.id WHERE  a.name LIKE CONCAT('%',:name,'%') AND a.email LIKE CONCAT('%',:email,'%')  AND r.name IN (:roles) order by created_at desc ", nativeQuery = true)
    List<Account> searchAccountByNameEmailRole1(@Param("name") String name, @Param("email") String email, @Param("roles") List<String> roles, Pageable pageable);


    @Query("SELECT a FROM Account  a WHERE a.id =?1 ")
    Account getAccountByIdAndRole(Long accountId);


}
