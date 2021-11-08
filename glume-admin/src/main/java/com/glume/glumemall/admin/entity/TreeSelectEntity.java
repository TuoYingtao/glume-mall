package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端下拉菜单树构建
 * @author tuoyingtao
 * @create 2021-11-08 11:19
 */
@Data
@TableName("sys_menu")
public class TreeSelectEntity implements Serializable {
    private static final Long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectEntity> children;

    public TreeSelectEntity(MenuEntity menuEntity) {
        this.id = menuEntity.getMenuId();
        this.label = menuEntity.getName();
        this.children = menuEntity.getChildren().stream().map(TreeSelectEntity::new).collect(Collectors.toList());
    }
}
