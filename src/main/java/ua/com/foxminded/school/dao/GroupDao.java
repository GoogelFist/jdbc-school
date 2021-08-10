package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.Group;

import java.util.Map;

public interface GroupDao {
    void create(Group group);

    Map<String, Integer> getByStudentsCount(int count);
}