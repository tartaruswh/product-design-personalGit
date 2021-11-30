package com.bewg.pd.common.util.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TreeNode<T extends TreeNode> implements Serializable {
    protected String id;
    protected String parentId;
    protected String name;
    protected List<T> children = new ArrayList<>();

    public void add(T node) {
        children.add(node);
    }
}
