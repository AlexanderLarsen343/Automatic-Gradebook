//Here
class HashItem<K, V> {
	private boolean _is_empty = true; //Use to check if the array is empty
	private V _item = null;
	private K _key = null;
	
	public HashItem()
	{
		//HashItem new_one = new HashItem();
	}
	
	public HashItem(K key, V value, boolean is_empty)
	{
		this._key = key;
		this._item = value;
		this._is_empty = is_empty;
	}
	
	public HashItem(K key, V value)
	{
		this._key = key;
		this._item = value;
	}
	
	public V getValue()
	{
		return _item;
	}
	
	public void setValue(V item)
	{
		this._item = item;
	}
	
	public K getKey()
	{
		return _key;
	}
	
	public void setKey(K key)
	{
		this._key = key;
	}
	
	public boolean isEmpty()
	{
		return _is_empty;
	}
	
	//Note: use this function wisely
	public boolean isTrueEmpty()
	{
		return _is_empty && _key == null && _item == null;
	}
	
	public void setIsEmpty(boolean value)
	{
		this._is_empty = value;
	}
}
