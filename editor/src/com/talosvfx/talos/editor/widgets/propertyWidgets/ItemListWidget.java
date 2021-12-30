package com.talosvfx.talos.editor.widgets.propertyWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.utils.Array;
import com.talosvfx.talos.TalosMain;
import com.talosvfx.talos.editor.widgets.ui.EditableLabel;
import com.talosvfx.talos.editor.widgets.ui.FilteredTree;
import com.talosvfx.talos.editor.widgets.ui.common.ColorLibrary;
import com.talosvfx.talos.editor.widgets.ui.common.SquareButton;


public abstract class ItemListWidget extends PropertyWidget<Array<ItemListWidget.ItemData>> {

    private FilteredTree<Object> list;

    public String defaultItemName = "New Item";

    public ItemListWidget(String name) {
        super(name);
    }

    @Override
    protected boolean isFullSize () {
        return true;
    }

    @Override
    public Array<ItemListWidget.ItemData> getValue () {
        return null;
    }

    @Override
    public Actor getSubWidget () {
        Table table = new Table();

        Skin skin = TalosMain.Instance().getSkin();

        Table topBar = new Table();
        topBar.setBackground(ColorLibrary.obtainBackground(getSkin(), ColorLibrary.BackgroundColor.DARK_GRAY));
        SquareButton newBtn = new SquareButton(skin, skin.getDrawable("timeline-btn-icon-new"));
        SquareButton deleteBtn = new SquareButton(skin, skin.getDrawable("timeline-btn-icon-delete"));
        topBar.add().expandX();
        topBar.add(newBtn).padRight(2);
        topBar.add(deleteBtn).padRight(2);

        list = new FilteredTree<>(skin, "modern");
        list.draggable = true;

        list.setItemListener(new FilteredTree.ItemListener() {
            @Override
            public void onNodeMove (FilteredTree.Node parentToMoveTo, FilteredTree.Node childThatHasMoved, int indexInParent, int indexOfPayloadInPayloadBefore) {
                valueChanged(makeDataArray());
            }

            @Override
            public void delete (Array<FilteredTree.Node> nodes) {
                deleteSelection();
            }
        });

        table.add(list).growX();
        table.row();
        table.add(topBar).growX().padLeft(-10).padRight(-10);

        newBtn.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                ItemData newItemData = new ItemData(defaultItemName, defaultItemName);
                Selection<FilteredTree.Node<Object>> selection = list.getSelection();
                FilteredTree.Node node;
                if(selection.size() > 0) {
                    int index = 0;
                    Array<FilteredTree.Node<Object>> rootNodes = list.getRootNodes();
                    for(index = 0; index < rootNodes.size; index++) {
                        if(rootNodes.get(index) == selection.first()) {
                            break;
                        }
                    }
                    node = addNode(newItemData, index + 1);
                } else {
                    node = addNode(newItemData);
                }

                valueChanged(makeDataArray());

                EditableLabel label = (EditableLabel) node.getActor();
                label.setEditMode();
            }
        });

        deleteBtn.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                deleteSelection();
            }
        });

        return table;
    }

    private void deleteSelection () {
        Selection<FilteredTree.Node<Object>> selection = list.getSelection();
        if(selection.size() > 0) {
            int index = 0;
            Array<FilteredTree.Node<Object>> rootNodes = list.getRootNodes();
            for(index = 0; index < rootNodes.size; index++) {
                if(rootNodes.get(index) == selection.first()) {
                    break;
                }
            }
            list.remove(selection.first());
            rootNodes = list.getRootNodes();
            if(rootNodes.size > 0) {
                selection.clear();
                index = index - 1;
                if(index < 0) index = 0;
                selection.add(rootNodes.get(index));
            }

            valueChanged(makeDataArray());
        }
    }

    private Array<ItemData> makeDataArray () {
        Array<ItemData> arr = new Array<>();
        Array<FilteredTree.Node<Object>> rootNodes = list.getRootNodes();
        for(FilteredTree.Node<Object> node: rootNodes) {
            EditableLabel label = (EditableLabel) node.getActor();
            arr.add(new ItemData(node.name, label.getText()));
        }
        return arr;
    }

    private FilteredTree.Node createNode(ItemData itemData) {
        Skin skin = TalosMain.Instance().getSkin();
        EditableLabel editableLabel = new EditableLabel(itemData.text, skin);
        editableLabel.setListener(new EditableLabel.EditableLabelChangeListener() {
            @Override
            public void changed (String newText) {
                valueChanged(makeDataArray());
            }
        });
        FilteredTree.Node node = new FilteredTree.Node(itemData.id, editableLabel);
        node.draggable = true;
        node.draggableInLayerOnly = true;

        return node;
    }

    private FilteredTree.Node addNode(ItemData itemData, int index) {
        FilteredTree.Node node = createNode(itemData);
        list.insert(index, node);

        return node;
    }

    private FilteredTree.Node addNode(ItemData itemData) {
        FilteredTree.Node node = createNode(itemData);
        list.add(node);

        return node;
    }

    @Override
    public void updateWidget (Array<ItemListWidget.ItemData> value) {
        list.clearChildren();
        for(ItemData item: value) {
            addNode(item);
        }
    }

    public static class ItemData {
        public String id;
        public String text;

        public ItemData(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public ItemData(String text) {
            this.id = text;
            this.text = text;
        }
    }
}
