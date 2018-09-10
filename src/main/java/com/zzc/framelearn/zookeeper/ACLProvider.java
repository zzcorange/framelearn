package com.zzc.framelearn.zookeeper;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;

public class ACLProvider implements org.apache.curator.framework.api.ACLProvider {

    @Override
    public List<ACL> getAclForPath( String path) {

         String firstLetter = String.valueOf(path.charAt(1));
         Id FIRST_USER_LETTER = new Id("digest", firstLetter);

         ACL acl = new ACL(ZooDefs.Perms.ALL, FIRST_USER_LETTER);
        return Collections.singletonList(acl);
    }

    @Override
    public List<ACL> getDefaultAcl() {
        throw new NotImplementedException();
    }

}