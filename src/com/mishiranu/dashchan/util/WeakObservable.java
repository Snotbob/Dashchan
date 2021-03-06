/*
 * Copyright 2014-2016 Fukurou Mishiranu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mishiranu.dashchan.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class WeakObservable<T> implements Iterable<T>
{
	private final ArrayList<WeakReference<T>> mObservers = new ArrayList<>();
	
	public void register(T observer)
	{
		mObservers.add(new WeakReference<>(observer));
	}
	
	public void unregister(T observer)
	{
		Iterator<WeakReference<T>> iterator = mObservers.iterator();
		while (iterator.hasNext())
		{
			T item = iterator.next().get();
			if (item == observer || item == null) iterator.remove();
		}
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return new WeakIterator();
	}
	
	private class WeakIterator implements Iterator<T>
	{
		private Iterator<WeakReference<T>> mIterator = mObservers.iterator();
		
		private T mNext;
		
		@Override
		public boolean hasNext()
		{
			if (mNext == null)
			{
				while (mIterator.hasNext())
				{
					T next = mIterator.next().get();
					if (next != null)
					{
						mNext = next;
						break;
					}
					else mIterator.remove();
				}
			}
			return mNext != null;
		}
		
		@Override
		public T next()
		{
			if (!hasNext()) throw new NoSuchElementException();
			T next = mNext;
			mNext = null;
			return next;
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}