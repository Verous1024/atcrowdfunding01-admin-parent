package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-18 下午 01:07
 */
public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAssignedIdByRoleId(Integer roleId);

    void saveRoleAuthRelathinship(Map<String, List<Integer>> map);
}
