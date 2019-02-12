
/**
 * @author bhartisharma
 *
 * @param <T>
 */
public class ProgramStack<T> {
	private int top;
	private T[] items;

	/**
	 * This is a constructor used to initialize the instance variables of a ProgramStack object
	 * @param size
	 */
	public ProgramStack(int size) {
		this.items = (T[]) new Object[size];
		this.top = -1;
	}

	/**
	 * Pushes an item on the stack
	 * @param item
	 */
	public void push(T item) {
		top++;
		items[top] = item;
		
	}

	/**
	 * Pops an item off the stack
	 * @return T
	 */
	public T pop() {
		if (top == -1) {
			return null;
		}
		T item = items[top];
		items[top] = null;
		top--;
		return item;
	}

	/**
	 * Returns the item at top of the stack
	 * @return T
	 */
	public T peek() {
		if ( top == -1 ) {
			return null;
		}
		return items[top];
	}

	/**
	 * Removes everything from the stack
	 */
	public void clear() {
		items = null;
	}
}
