 package joptsimple.internal;
 
 import java.util.Map;
 import java.util.TreeMap;
 
 public class AbbreviationMap<V>
 {
   private String key;
   private V value;
   private final Map<Character, AbbreviationMap<V>> children = new TreeMap<Character, AbbreviationMap<V>>();
   private int keysBeyond;
 
   public boolean contains(String aKey)
   {
     return get(aKey) != null;
   }
 
   public V get(String aKey)
   {
     char[] chars = charsOf(aKey);
 
     AbbreviationMap<V> child = this;
     for (char each : chars) {
       child = child.children.get(Character.valueOf(each));
       if (child == null) {
         return null;
       }
     }
     return child.value;
   }
 
   public void put(String aKey, V newValue)
   {
     if (newValue == null)
       throw new NullPointerException();
     if (aKey.length() == 0) {
       throw new IllegalArgumentException();
     }
     char[] chars = charsOf(aKey);
     add(chars, newValue, 0, chars.length);
   }
 
   public void putAll(Iterable<String> keys, V newValue)
   {
     for (String each : keys)
       put(each, newValue);
   }
 
   private boolean add(char[] chars, V newValue, int offset, int length) {
     if (offset == length) {
       this.value = newValue;
       boolean wasAlreadyAKey = this.key != null;
       this.key = new String(chars);
       return !wasAlreadyAKey;
     }
 
     char nextChar = chars[offset];
     AbbreviationMap<V> child = this.children.get(Character.valueOf(nextChar));
     if (child == null) {
       child = new AbbreviationMap<V>();
       this.children.put(Character.valueOf(nextChar), child);
     }
 
     boolean newKeyAdded = child.add(chars, newValue, offset + 1, length);
 
     if (newKeyAdded) {
       this.keysBeyond += 1;
     }
     if (this.key == null) {
       this.value = (this.keysBeyond > 1 ? null : newValue);
     }
     return newKeyAdded;
   }
 
   public Map<String, V> toJavaUtilMap()
   {
     TreeMap<String, V> mappings = new TreeMap<String, V>();
     addToMappings(mappings);
     return mappings;
   }
 
   private void addToMappings(Map<String, V> mappings) {
     if (this.key != null) {
       mappings.put(this.key, this.value);
     }
     for (AbbreviationMap<V> each : this.children.values())
       each.addToMappings(mappings);
   }
 
   private static char[] charsOf(String aKey) {
     char[] chars = new char[aKey.length()];
     aKey.getChars(0, aKey.length(), chars, 0);
     return chars;
   }
 }

/* Location:           C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar
 * Qualified Name:     joptsimple.internal.AbbreviationMap
 * JD-Core Version:    0.6.2
 */