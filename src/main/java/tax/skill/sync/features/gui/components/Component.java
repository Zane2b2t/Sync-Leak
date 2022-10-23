package tax.skill.sync.features.gui.components;

import tax.skill.sync.Sync;
import tax.skill.sync.features.Feature;
import tax.skill.sync.features.gui.SyncGui;
import tax.skill.sync.features.gui.components.items.Item;
import tax.skill.sync.features.gui.components.items.buttons.Button;
import tax.skill.sync.features.modules.client.ClickGui;
import tax.skill.sync.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Component
        extends Feature {
    public static int[] counter1 = new int[]{1};
    private final ArrayList<Item> items = new ArrayList();
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden = false;

    public Component(String name, int x, int y, boolean open) {
        super(name);
        this.x = x;
        this.y = y;
        this.width = 88;
        this.height = 18;
        this.open = open;
        this.setupItems();
    }

    public void setupItems() {
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float totalItemHeight = this.open ? this.getTotalItemHeight ( ) - 2.0f : 0.0f;
        int color = new Color (ClickGui.getInstance().red.getValue(),ClickGui.getInstance().blue.getValue(),ClickGui.getInstance().green.getValue()).getRGB();
            RenderUtil.drawRect ( this.x , (float) this.y - 1.5f , this.x + this.width , this.y + this.height - 6 , color );
        if ( this.open ) {
            RenderUtil.drawRect ( this.x , (float) this.y + 12.5f , this.x + this.width , (float) ( this.y + this.height ) + totalItemHeight , 0x77000000 );
            if ( ClickGui.getInstance ( ).outline.getValue ( ) ) {
                if ( ClickGui.getInstance ( ).rainbowRolling.getValue ( ) ) {
                    GlStateManager.disableTexture2D ( );
                    GlStateManager.enableBlend ( );
                    GlStateManager.disableAlpha ( );
                    GlStateManager.tryBlendFuncSeparate ( GlStateManager.SourceFactor.SRC_ALPHA , GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA , GlStateManager.SourceFactor.ONE , GlStateManager.DestFactor.ZERO );
                    GlStateManager.shadeModel ( 7425 );
                    GL11.glBegin ( 2 );
                    Color outlineColor =  new Color ( Sync.colorManager.getColorAsIntFullAlpha ( ) );
                    GL11.glColor4f ( (float) outlineColor.getRed ( ) , (float) outlineColor.getGreen ( ) , (float) outlineColor.getBlue ( ) , (float) outlineColor.getAlpha ( ) );
                    GL11.glVertex3f ( (float) this.x , (float) this.y - 1.5f , 0.0f );
                    GL11.glVertex3f ( (float) ( this.x + this.width ) , (float) this.y - 1.5f , 0.0f );
                    GL11.glVertex3f ( (float) ( this.x + this.width ) , (float) ( this.y + this.height ) + totalItemHeight , 0.0f );
                    GL11.glVertex3f ( (float) this.x , (float) ( this.y + this.height ) + totalItemHeight , 0.0f );
                    GL11.glEnd ( );
                    GlStateManager.shadeModel ( 7424 );
                    GlStateManager.disableBlend ( );
                    GlStateManager.enableAlpha ( );
                    GlStateManager.enableTexture2D ( );
                }
            }
        }
        Sync.textManager.drawStringWithShadow ( this.getName ( ) , (float) this.x + 3.0f , (float) this.y - 4.0f - (float) SyncGui.getClickGui ( ).getTextOffset ( ) , - 1 );
        if ( this.open ) {
            float y = (float) ( this.getY ( ) + this.getHeight ( ) ) - 3.0f;
            for (Item item : this.getItems ( )) {
                if ( item.isHidden ( ) ) continue;
                item.setLocation ( (float) this.x + 2.0f , y );
                item.setWidth ( this.getWidth ( ) - 4 );
                item.drawScreen ( mouseX , mouseY , partialTicks );
                y += (float) item.getHeight ( ) + 1.5f;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            SyncGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : this.getItems()) {
            height += (float) item.getHeight() + 1.5f;
        }
        return height;
    }
}

