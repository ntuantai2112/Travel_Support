package travs.service;

import travs.response.BaseResponse;
import travs.request.activities.ActivitiesGameUploadBody;
import travs.request.activities.ActivitiesUploadBody;

public interface ActivitiesService {

    BaseResponse get(String slug);

    BaseResponse uploadActivities(ActivitiesUploadBody activitiesUploadBody) ;

    BaseResponse updateActivities(ActivitiesUploadBody activitiesUploadBody);

    BaseResponse uploadGameActivities(ActivitiesGameUploadBody activitiesGameUploadBody);

    BaseResponse updateGameActivities(ActivitiesGameUploadBody activitiesGameUploadBody);

    void deleteActivities(String activitiesCode);

    void deleteGameActivities(Integer id);

    BaseResponse getListActivities(Integer page, Integer perPage);

}
