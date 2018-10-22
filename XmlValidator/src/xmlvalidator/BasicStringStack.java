package xmlvalidator;

import static java.lang.System.*;

public class BasicStringStack implements StringStack {

	private int count;
	private String[] items;


	public BasicStringStack() {
		count = 0;
		items = new String[10];
	}


	public BasicStringStack(int initialsize) {
		count = 0;
		items = new String[initialsize];
	}


	@Override
	public void push(String item) {
		if (count == items.length) {
			int newLength = (int) (items.length * 1.25) + 1;
			String[] tempAry = new String[newLength];
			arraycopy(items, 0, tempAry, 0, items.length);
			items = tempAry;
		}
		items[count++] = item;
	}


	@Override
	public String pop() {
		String tempItem;

		if (count == 0)
			return null;
		else {
			tempItem = items[count - 1];
			items[count] = null;
			count--;
			return tempItem;
		}
	}


	@Override
	public String peek(int position) {
		if ((position > count - 1) || (position < 0))
			return null;
		else
			return items[count - position - 1];
	}


	@Override
	public int getCount() {
		return this.count;
	}


	public void clear() {
		for (int i = 0; i < this.getCount(); i++) {
			this.items[i] = null;
		}
	}

}
