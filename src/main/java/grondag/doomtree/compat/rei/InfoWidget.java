/*******************************************************************************
 * Copyright (C) 2019 grondag
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
		lines = font.wrapStringToWidthAsList(text, width);
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
		for (final String text : lines) {
			font.draw(format + text, x, y, -1);
			y += font.fontHeight + 2;
		}
	}
}
