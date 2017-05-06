package ProgramsPorblems;


	public class GenericType<T> {

		Object a[] =null;
		int index=0;
		
		public GenericType() {
			a =new Object[10];
		}
		
		public boolean add(T item) {
			a[index++]=item;
			return true;
		}
		
		public T get(int i) {
			return (T)a[i];
		}
		
		
		public int size() {
			return index;
		}
	}

