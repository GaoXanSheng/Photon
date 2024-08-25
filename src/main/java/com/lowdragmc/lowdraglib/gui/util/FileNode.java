package com.lowdragmc.lowdraglib.gui.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/FileNode.class */
public class FileNode extends TreeNode<File, File> {
    public FileNode(File dir) {
        this(0, dir);
    }

    private FileNode(int dimension, File key) {
        super(dimension, key);
    }

    @Override // com.lowdragmc.lowdraglib.gui.util.TreeNode
    public boolean isLeaf() {
        return getKey().isFile();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.util.TreeNode
    public File getContent() {
        if (isLeaf()) {
            return getKey();
        }
        return null;
    }

    @Override // com.lowdragmc.lowdraglib.gui.util.TreeNode
    public List<TreeNode<File, File>> getChildren() {
        if (this.children == null && !isLeaf()) {
            this.children = new ArrayList();
            Arrays.stream(((File) this.key).listFiles()).sorted(a, b -> {
                if (a.isFile() && b.isFile()) {
                    return a.compareTo(b);
                }
                if (a.isDirectory() && b.isDirectory()) {
                    return a.compareTo(b);
                }
                if (a.isDirectory()) {
                    return -1;
                }
                return 1;
            }).forEach(file -> {
                this.children.add(new FileNode(this.dimension + 1, file).setValid(this.valid));
            });
        }
        return super.getChildren();
    }

    @Override // com.lowdragmc.lowdraglib.gui.util.TreeNode
    public String toString() {
        return getKey().getName();
    }
}
