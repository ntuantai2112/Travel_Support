package travs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import travs.constant.Constants;
import travs.entity.account.Account;
import travs.entity.account.Role;
import travs.request.account.AccountRequest;
import travs.response.account.AccountResponse;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "gender", expression = "java(mapGender(account.getGender()))")
    AccountResponse toResponse(Account account);


    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "role", expression = "java(mapRole(request.getRoleId()))")
    Account toEntity(AccountRequest request);


    @Mapping(target = "role", expression = "java(mapRole(request.getRoleId()))")
    void updateEntityFromRequest(AccountRequest request, @MappingTarget Account account);


    default String mapGender(Boolean gender) {
        if (Boolean.TRUE.equals(gender)) {
            return Constants.AccountGender.GENDER_MALE;
        } else {
            return Constants.AccountGender.GENDER_FEMALE;
        }
    }


    // Phương thức ánh xạ từ String -> Role
    default Role mapRole(Long roleId) {
        if (roleId == null) return null;
        return Role.builder().id(roleId).build();
    }

}
