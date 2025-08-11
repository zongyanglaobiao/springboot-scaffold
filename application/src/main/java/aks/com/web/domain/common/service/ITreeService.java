package aks.com.web.domain.common.service;

import aks.com.web.domain.common.entity.TreeEntity;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jamesaks
 * @since 2025/8/5
 */
public interface ITreeService<E extends TreeEntity<E>> extends IBaseService<E>{
    default List<E> getTree() {
        return getTree(list());
    }

    default List<E> getTree(List<E> list) {
        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        // 用于缓存所有节点
        Map<String, E> idMap = list.stream().collect(Collectors.toMap(E::getId, menu -> menu));
        List<E> roots = new ArrayList<>();
        //组成树
        for (E node : list) {
            String parentId = node.getParentId();
            if (StrUtil.isBlank(parentId)) {
                roots.add(node);
            } else {
                E parent = idMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }

    default Boolean saveOrUpdateTree(E entity) {
        if (StrUtil.isNotBlank(entity.getId())) {
            return updateById(entity);
        }
        boolean isSaveSuccess = save(entity);
        String path;
        if (StrUtil.isNotBlank(entity.getParentId())) {
            path = getByIdThrowIfNull(entity.getParentId()).getPath() + "|" + entity.getId();
        } else {
            path = entity.getId();
        }

        //当前节点设置 path
        entity.setPath(path);
        return isSaveSuccess && updateById(entity);
    }

    default Boolean removeTreeById(Serializable id) {
        //TODO 按道理这里只返回 ID
        List<E> nodeIds = listByNodeId(id);
        return !nodeIds.isEmpty() && removeByIds(nodeIds);
    }

    /**
     * 将当前节点和子节点平摊开
     */
    default List<E> listByNodeId(Serializable id) {
        //包含此 ID 的 PATH
        List<E> list = this.lambdaQuery().like(TreeEntity::getPath, id).list();
        List<String> nodeIds = list.stream().map(t -> parsePath(t.getPath(), String.valueOf(id)))
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        return nodeIds.isEmpty() ? new ArrayList<>() : listByIds(nodeIds);
    }

    private List<String> parsePath(String path,String id) {
        if (StrUtil.isBlank(path) || !path.contains("|")) {
            return new ArrayList<>();
        }

        List<String> result = Arrays.stream(path.split("\\|")).toList();
        int index = result.indexOf(id);
        if (index < 0) {
            return new ArrayList<>();
        }
        return result.subList(index, result.size());
    }
}
