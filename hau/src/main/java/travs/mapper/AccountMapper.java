package travs.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import travs.constant.Constants;
import travs.entity.account.Account;
import travs.request.account.AccountRequest;
import travs.response.account.AccountResponse;
import travs.utils.FileStore;
import travs.utils.PasswordGenerator;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "gender", expression = "java(mapGender(account.getGender()))")
    AccountResponse toResponse(Account account);


    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "role", expression = "java(new Role(5L))")
    Account toEntity(AccountRequest request);


    default String mapGender(Boolean gender) {
        if (Boolean.TRUE.equals(gender)) {
            return Constants.AccountGender.GENDER_MALE;
        } else {
            return Constants.AccountGender.GENDER_FEMALE;
        }
    }

    @AfterMapping
    default void afterToEntity(AccountRequest request, @MappingTarget Account account) {
        account.setImage(FileStore.getDefaultAvatar());
        account.setPassword(PasswordGenerator.getHashString(request.getPassword()));
    }
}
