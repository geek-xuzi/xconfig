package com.xuen.xconfig.util;

import java.util.StringJoiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.ZKUtil;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public class ZkUtils {

    private static final StringJoiner result = new StringJoiner("/");

    public static void createForPath(String path, byte[] data, CuratorFramework zkClient) {
        String[] split = path.split("/");
        String path1 = "/";
        try {
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (!StringUtils.isEmpty(s)) {
                    path1 += s;
                    if (i == split.length - 1) {
                        zkClient.create().forPath(path1);
                        zkClient.setData().forPath(path1, data);
                    } else {
                        zkClient.create().forPath(path1);
                        path1 += "/";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createForPath(String path, CuratorFramework zkClient) {
        String[] split = path.split("/");
        String path1 = "/";
        try {
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (!StringUtils.isEmpty(s)) {
                    path1 += s;
                    if (i != split.length - 1) {
                        path1 += "/";
                    }
                    zkClient.create().forPath(path1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFoePath(String path, CuratorFramework zkClient) {
        try {
            ZKUtil.deleteRecursive(zkClient.getZookeeperClient().getZooKeeper(), path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
