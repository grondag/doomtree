package grondag.doomtree.compat.rei;

import java.util.Collections;
import java.util.List;

import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.gui.widget.WidgetWithBounds;
import net.minecraft.client.gui.Element;

public class InfoWidget extends WidgetWithBounds {
    
    public int x;
    public int y;
    public int width;
    public int height;
    public String format;
    public List<String> lines;
    
    public InfoWidget(int x, int y, int width, int height, String text, String format) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lines = font.wrapStringToWidthAsList(text, width);
        this.format = format;
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }
    
    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }
    
    @Override
    public void render(int mouseX, int mouseY, float delta) {
    	int y = this.y;
    	for (String text : lines) {
    		font.draw(format + text, x, y, -1);
    		y += font.fontHeight + 2;
    	}
    }
}
