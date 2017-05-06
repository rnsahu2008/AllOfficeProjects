
package Test;

import java.util.Comparator;

 class Comperator1  implements Comparator<DataObject>
{
		@Override
		public int compare(DataObject s1, DataObject s2) {
				return s1.productPrice.compareTo(s2.productPrice);
	}


	
	
}