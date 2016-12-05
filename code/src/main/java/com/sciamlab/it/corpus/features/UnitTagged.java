package com.sciamlab.it.corpus.features;

// left:	token 
// right:	tag

public class UnitTagged<L,R>{

	private final L left;
	private final R right;

	public UnitTagged(L left, R right){
		this.left = left;
		this.right = right;
	}

	public L getLeft(){ 
		return left; 
	}
	
	public R getRight(){ 
		return right; 
	}

	@Override
	public int hashCode(){
		return left.hashCode() ^ right.hashCode(); 
	}

	public String toString(){
		return "token: "+left+"                    tag:"+right;
	}
}