package com.um.springbootprojstructure.dto.legacy;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class LegacyUsersExport {

    @XmlElement(name = "user")
    private List<LegacyUserRecord> users = new ArrayList<>();

    public List<LegacyUserRecord> getUsers() { return users; }
    public void setUsers(List<LegacyUserRecord> users) { this.users = users; }
}
