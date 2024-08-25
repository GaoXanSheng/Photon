package com.lowdragmc.lowdraglib.gui.util;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import net.minecraft.util.Tuple;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/TreeBuilder.class */
public class TreeBuilder<K, V> {
    protected final Stack<TreeNode<K, V>> stack = new Stack<>();

    public TreeBuilder(K key) {
        this.stack.push(new TreeNode<>(0, key));
    }

    public static <K, V> TreeBuilder<K, V> start(K key) {
        return new TreeBuilder<>(key);
    }

    public TreeBuilder<K, V> branch(K key, Consumer<TreeBuilder<K, V>> builderConsumer) {
        List<TreeNode<K, V>> children = this.stack.peek().getChildren();
        if (children != null && !children.isEmpty()) {
            for (TreeNode<K, V> child : children) {
                if (!child.isLeaf() && child.key.equals(key)) {
                    this.stack.push(child);
                    builderConsumer.accept(this);
                    endBranch();
                    return this;
                }
            }
        }
        this.stack.push(this.stack.peek().getOrCreateChild(key));
        builderConsumer.accept(this);
        endBranch();
        return this;
    }

    public TreeBuilder<K, V> startBranch(K key) {
        this.stack.push(this.stack.peek().getOrCreateChild(key));
        return this;
    }

    public TreeBuilder<K, V> endBranch() {
        this.stack.pop();
        return this;
    }

    public TreeBuilder<K, V> leaf(K key, V content) {
        this.stack.peek().addContent(key, content);
        return this;
    }

    public TreeBuilder<K, V> remove(K key) {
        this.stack.peek().removeChild(key);
        return this;
    }

    public TreeNode<K, V> build() {
        while (this.stack.size() > 1) {
            this.stack.pop();
        }
        return this.stack.peek();
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/TreeBuilder$Menu.class */
    public static class Menu extends TreeBuilder<Tuple<IGuiTexture, String>, Runnable> {
        public static Tuple<IGuiTexture, String> CROSS_LINE = new Tuple<>(IGuiTexture.EMPTY, "");

        private Menu(Tuple<IGuiTexture, String> key) {
            super(key);
        }

        public static Menu start() {
            return new Menu(new Tuple(IGuiTexture.EMPTY, ""));
        }

        public Menu crossLine() {
            this.stack.peek().createChild((K) CROSS_LINE);
            return this;
        }

        public Menu branch(IGuiTexture icon, String name, Consumer<Menu> menuConsumer) {
            branch((Menu) new Tuple(icon, name), (Consumer<TreeBuilder<Menu, V>>) builder -> {
                menuConsumer.accept(this);
            });
            return this;
        }

        public Menu branch(String name, Consumer<Menu> menuConsumer) {
            List<TreeNode<K, V>> children = this.stack.peek().getChildren();
            if (children != null && !children.isEmpty()) {
                for (TreeNode<K, V> treeNode : children) {
                    if (!treeNode.isLeaf() && ((String) ((Tuple) treeNode.getKey()).m_14419_()).equals(name)) {
                        this.stack.push(treeNode);
                        menuConsumer.accept(this);
                        endBranch2();
                        return this;
                    }
                }
            }
            return branch(IGuiTexture.EMPTY, name, menuConsumer);
        }

        @Override // com.lowdragmc.lowdraglib.gui.util.TreeBuilder
        /* renamed from: endBranch */
        public TreeBuilder<Tuple<IGuiTexture, String>, Runnable> endBranch2() {
            super.endBranch();
            return this;
        }

        public Menu leaf(IGuiTexture icon, String name, Runnable runnable) {
            super.leaf((Menu) new Tuple(icon, name), (Tuple) runnable);
            return this;
        }

        public Menu leaf(String name, Runnable runnable) {
            super.leaf((Menu) new Tuple(IGuiTexture.EMPTY, name), (Tuple) runnable);
            return this;
        }

        public Menu remove(String name) {
            List<TreeNode<K, V>> children = this.stack.peek().getChildren();
            if (children != null && !children.isEmpty()) {
                for (TreeNode<K, V> treeNode : children) {
                    if (((String) ((Tuple) treeNode.getKey()).m_14419_()).equals(name)) {
                        this.stack.peek().removeChild((K) ((Tuple) treeNode.getKey()));
                        return this;
                    }
                }
            }
            return this;
        }

        public static IGuiTexture getIcon(Tuple<IGuiTexture, String> key) {
            return (IGuiTexture) key.m_14418_();
        }

        public static String getName(Tuple<IGuiTexture, String> key) {
            return (String) key.m_14419_();
        }

        public static void handle(TreeNode<Tuple<IGuiTexture, String>, Runnable> node) {
            if (node.isLeaf() && node.getContent() != null) {
                node.getContent().run();
            }
        }

        public static boolean isCrossLine(Tuple<IGuiTexture, String> key) {
            return key == CROSS_LINE;
        }
    }
}
