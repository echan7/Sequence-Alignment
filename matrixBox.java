
public class matrixBox {
	public IntegerObj M;
	public IntegerObj Ix;
	public IntegerObj Iy;

	public matrixBox(int j, int i) {
		M = new IntegerObj(j, i);
		Ix = new IntegerObj(j, i);
		Iy = new IntegerObj(j, i);
	}
	
	public IntegerObj getMax(){
		IntegerObj maxObj = M;
		if(maxObj.num < Ix.num) {
			maxObj = Ix;
		} 
		if(maxObj.num < Iy.num) {
			maxObj = Iy;
		}
		
		return maxObj;
	}
}
