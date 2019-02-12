import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author bhartisharma
 *
 */
public class ExpressionEvaluator {
	/**
	 * @author bhartisharma
	 *
	 * @param <T>
	 */
	static class InputQueue<T> {
		private int front;
		private int back;
		private T[] queue;

		public InputQueue() {
			queue = (T[]) new Object[2];
			front = -1;
			back = -1;
		}

		/**
		 * Pushes the element in the queue
		 * @param item
		 */
		public void enqueue(T item) {
			if (queue.length == size() + 1) {
				T[] old = queue;
				queue = (T[]) new Object[(size() + 1) * 2];
				for (int i = 0; i <= size(); i++) {
					queue[i] = old[i];
				}
			}
			if (front == -1) {
				front++;
			}
			back++;
			queue[back] = item;
		}

		/**
		 * Removes the element from the queue
		 * @return T
		 */
		public T dequeue() {
			if (front == -1 || back == -1) {
				return null;
			}
			T item = queue[front];
			queue[front] = null;
			front++;
			return item;
		}

		/**
		 * Return the size of the queue
		 * @return size
		 */
		public int size() {
			return back - front;
		}
	}

	public static void main(String[] args) throws IOException {

		File fl = new File(args[0]);
		BufferedReader br = new BufferedReader(new FileReader(fl));

		InputQueue<String> inputeQueue = new InputQueue<String>();
		SymbolTable<String> hashTable = new SymbolTable<String>();
		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			inputeQueue.enqueue(line);
		}
		while (inputeQueue.size() != -1) {
			String expression = inputeQueue.dequeue();
			String value = evaluateExpression(expression, hashTable);
			System.out.println("Input expression: " + expression);
			System.out.println("Value: " + value);
			if (hashTable.size() != 0) {
				if (!value.equals("INVALID expression format")) {
					System.out.println("Symbol table entries: " + hashTable.toString());
				}
				hashTable.clear();
			}
			System.out.println();
		}
		br.close();
	}

	/**
	 * This will evaluate the expression and will return the evaluated value
	 * 
	 * @param expression
	 * @param hashTable
	 * @return String
	 */
	private static String evaluateExpression(String expression, SymbolTable<String> hashTable) {
		String[] expressionList = expression.split(" ");
		ProgramStack<String> operandStack = new ProgramStack<String>(expressionList.length);
		Pattern notNumber = Pattern.compile("[a-z]|[A-Z]");
		Pattern number = Pattern.compile("[0-9]");

		for (int i = 0; i < expressionList.length; i++) {
			if (number.matcher(expressionList[i]).matches()) {
				operandStack.push(expressionList[i]);
			} else if (expressionList[i].matches("\\+|-|\\*|/|\\^")) {
				String operand2 = operandStack.pop();
				String operand1 = operandStack.pop();
				if (operand1 == null || operand2 == null) {
					return "INVALID expression format";
				}
				if (notNumber.matcher(operand2).find()) {
					operand2 = hashTable.get(operand2);
				}

				if (notNumber.matcher(operand1).find()) {
					operand1 = hashTable.get(operand1);
				}
				operandStack.push(calculateExp(operand1, operand2, expressionList[i]));
			} else if (expressionList[i].matches("=|\\+=|-=|\\*=|/=")) {
				String value = "";
				String variable = "";
				String op = calculateAssignmentOp(expressionList[i]);
				if (op.equals("=")) {
					value = operandStack.pop();
					variable = operandStack.pop();
				} else {
					String operand2 = operandStack.pop();
					String operand1 = operandStack.pop();
					if (operand1 == null || operand2 == null) {
						return "INVALID expression format";
					}
					if (notNumber.matcher(operand2).find()) {
						variable = operand2;
						operand2 = hashTable.get(operand2);
					}

					if (notNumber.matcher(operand1).find()) {
						variable = operand1;
						operand1 = hashTable.get(operand1);

					}

					value = calculateExp(operand1, operand2, op);

				}
				if (variable != null) {
					hashTable.put(variable, value);
				}

			} else {
				operandStack.push(expressionList[i]);
			}
		}

		String finalOutput = operandStack.pop();
		if (finalOutput != null && operandStack.pop() != null) {
			return "INVALID expression format";
		}
		if ( finalOutput == null ) {
			return "INVALID expression format";
		}
		if (notNumber.matcher(finalOutput).find()) {
			return "INVALID expression format";
		}
		return finalOutput;
	}

	/**
	 * 
	 * This will check the operation and will perform the operation on the 2 operand
	 * @param operand1
	 * @param operand2
	 * @param operation
	 * @return String
	 */
	private static String calculateExp(String operand1, String operand2, String operation) {
		String value = "";
		switch (operation) {
		case "+":
			value = String.valueOf(Integer.parseInt(operand1) + Integer.parseInt(operand2));
			break;
		case "-":
			value = String.valueOf(Integer.parseInt(operand1) - Integer.parseInt(operand2));
			break;
		case "/":
			value = String.valueOf((int) (Integer.parseInt(operand1) / Integer.parseInt(operand2)));
			break;
		case "*":
			value = String.valueOf(Integer.parseInt(operand1) * Integer.parseInt(operand2));
			break;
		case "^":
			value = String.valueOf((int) Math.pow(Integer.parseInt(operand1), Integer.parseInt(operand2)));
			break;
		}
		return value;
	}

	/**
	 * This will check for the assignment and will return the operation 
	 * @param operation
	 * @return String
	 */
	private static String calculateAssignmentOp(String operation) {
		String value = "";
		switch (operation) {
		case "+=":
			value = "+";
			break;
		case "-=":
			value = "-";
			break;
		case "/=":
			value = "/";
			break;
		case "*=":
			value = "*";
			break;
		case "=":
			value = "=";
			break;
		}
		return value;
	}

}
