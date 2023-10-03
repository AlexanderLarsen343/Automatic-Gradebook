//Here
class LinearHashTable<K, V> extends HashTableBase<K, V>
{
	//determines whether or not we need to resize
	//to turn off resize, just always return false
    protected boolean needsResize()
    {
    	//linear probing seems to get worse after a load factor of about 70%
		if (_number_of_elements > (0.7 * _primes[_local_prime_index]))
		{
			return true;
		}
		return false;
    }
    
    //called to check to see if we need to resize
    protected void resizeCheck()
    {
    	//Right now, resize when load factor > .7; it might be worth it to experiment with 
		//this value for different kinds of hashtables
		if (needsResize())
		{
			_local_prime_index++;

			HasherBase<K> hasher = _hasher;
			LinearHashTable<K, V> new_hash = new LinearHashTable<K, V>(hasher, _primes[_local_prime_index]);
			
			for (HashItem<K, V> item: _items)
			{
				if (item.isEmpty() == false)
				{
					//add to new hash table
					new_hash.addElement(item.getKey(), item.getValue());
				}
			}
			
			_items = new_hash._items;
		}
    }
    
    public LinearHashTable()
    {
    	super();
    }
    
    public LinearHashTable(HasherBase<K> hasher)
    {
    	super(hasher);
    }
    
    public LinearHashTable(HasherBase<K> hasher, int number_of_elements)
    {
    	super(hasher, number_of_elements);
    }
    
    //copy constructor
    public LinearHashTable(LinearHashTable<K, V> other)
    {
    	super(other);
    }
    
    //concrete implementation for parent's addElement method
    public void addElement(K key, V value)
    {
    	//check for size restrictions
    	resizeCheck();
    	
    	//calculate initial hash based on key
    	int hash = super.getHash(key);
		
		//Two ints to make sure I go through the whole vector
		int size = _items.size();
		int loopCount = 0;

		//Created the new hashitem we are going to add
		HashItem<K, V> newHashItem = new HashItem<>(key, value, false);

		//Do loop to go through the whole vector
		do{
			//Doesn't add duplicates
			if(_items.get(hash).equals(newHashItem)){
				return;
			}
			//if spot is not flag, add element
			if(_items.get(hash).isEmpty()){
				_items.set(hash, newHashItem);
				_number_of_elements++;
				return;
			//else update hash and keep traversing
			} else {
				hash = (hash + 1) % _items.size();
				loopCount++;
			}
	
		} while (loopCount != _items.size());
    }
    
    //removes supplied key from hash table
    public void removeElement(K key)
    {
    	//calculate hash
    	int hash = super.getHash(key);
		int loopCount = 0;
    	
		//If the hashtable doesn't even have the element we are trying to remove then stop
		if(this.containsElement(key) != true){
			return;
		}
		
		do{
			//Points to current hash item
			HashItem<K, V> slot = _items.elementAt(hash);

			//if spot is not flag, and we found the correct key, remove hash item by flipping flag
			if(_items.get(hash).isEmpty() != true && _items.get(hash).getKey().equals(key)){

				//Creates new HashItem with the same key, but flips the flag
				HashItem<K, V> newHashItem = new HashItem<>(_items.get(hash).getKey(), _items.get(hash).getValue(), true);
				_items.set(hash, newHashItem);
				_number_of_elements--;
				return;

				//else update hash and keep traversing
			} else {
				hash = (hash + 1) % _items.size();
				loopCount++;
			}
	
		} while (loopCount != _items.size());
    }
    
    //returns true if the key is contained in the hash table
    public boolean containsElement(K key)
    {
    	int hash = super.getHash(key);
		int loopCount = 0;

		//Loop that will keep going until it hits the right key or unflagged box
		do {
			HashItem<K, V> slot = _items.elementAt(hash);

			//Try catch block for catching nullpointer exception
			try{
				//If the box has what we are looking for and its flagged, return true
				if(slot.getKey().equals(key) && (_items.get(hash).isEmpty() != true)){
					return true;
				}
			}catch(NullPointerException e){
				
			}

			//If box is unflagged, return false
			if(_items.get(hash).isTrueEmpty() == true){
				return false;
			}

			//Keeps the loop traversing through the hashtable
			hash = (hash + 1) % _items.size();
			loopCount++;
		} while (loopCount != _items.size());

		//If the loop went around the whole table, it means its not in there
    	return false;
    }
    
    //returns the item pointed to by key
    public V getElement(K key)
    {
		//Hash my the key
    	int hash = super.getHash(key);
		int loopCount = 0;

		//If hashItem is not in hashtable, return null
		if(this.containsElement(key) != true){
			return null;
		}

		do {
			HashItem<K, V> slot = _items.elementAt(hash);
			//Try catch block for catching nullpointer exception

			try{
				//If the box has what we are looking for and its flagged, return the value we looking for
				if(slot.getKey().equals(key) && (_items.get(hash).isEmpty() != true)){
					return _items.elementAt(hash).getValue();
				}
			}catch(NullPointerException e){
			}

			//Keeps the loop traversing through the hashtable
			hash = (hash + 1) % _items.size();
			loopCount++;
		} while (loopCount != _items.size());

		//Its impossible for the code to get to this point but the method requires it be here
    	return null;
    }

	@Override
	public String toString() {
		return "LinearHashTable []";
	}

}