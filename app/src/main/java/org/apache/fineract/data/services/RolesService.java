package org.apache.fineract.data.services;

import org.apache.fineract.data.models.rolesandpermission.Role;
import org.apache.fineract.data.remote.EndPoints;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
public interface RolesService {

    @GET(EndPoints.API_IDENTITY_PATH + "/roles")
    Observable<List<Role>> getRoles();
}
