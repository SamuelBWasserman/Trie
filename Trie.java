package structures;

import java.util.ArrayList;

/**
 * This class implements a compressed trie. Each node of the tree is a CompressedTrieNode, with fields for
 * indexes, first child and sibling.
 * 
 *
 */
public class Trie {
	
	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a compressed trie with words to be indexed, and root node set to
	 * null fields.
	 * 
	 * @param words
	 */
	public Trie() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}

	
	/**
	 * Inserts a word into this trie. Converts to lower case before adding.
	 * The word is first added to the words array list, then inserted into the trie.
	 * 
	 * @param word Word to be inserted.
	 */
	//WORDS1.TXT DOES NOT WORK FIX MUST FIX 
	public void insertWord(String word) {
		/** COMPLETE THIS METHOD **/
		//changes the string to all lowercase for insertion
		word=word.toLowerCase();
		if(root.firstChild==null)
		{
			words.add(word);
			root.firstChild=new TrieNode(new Indexes(0,(short)0,(short)(word.length()-1)),null,null);
		}
		else
		{
			TrieNode temp=root.firstChild;
			int startIndex=0;
			words.add(word);
			while(temp!=null)
			{
				//Loop that finds the common prefix...If there's no prefix then it will move on to the next Node
				String prefix="";
				String currentNode=words.get(temp.substr.wordIndex);
				for(int i=temp.substr.startIndex;i<word.length();i++)
				{
					if(word.charAt(i)==currentNode.charAt(i))
					{
						prefix+=word.charAt(i);
					}
					else
						break;
				}
				//If a common prefix is found...
				if(prefix.length()>=1)
				{
					if(temp.firstChild==null)
					{
						//fix up the index
						temp.substr.endIndex=(short)(temp.substr.startIndex+prefix.length()-1);
						temp.substr.startIndex=(short)(temp.substr.startIndex);
						//add two children for trailing substrings
						temp.firstChild=new TrieNode(new Indexes(words.indexOf(currentNode), (short)(temp.substr.startIndex+prefix.length()),(short)(currentNode.length()-1)),null,null);
						temp.firstChild.sibling=new TrieNode(new Indexes(words.indexOf(word),(short)(temp.substr.startIndex+prefix.length()),(short)(word.length()-1)),null,null);
						break;
					}
					
					//if theres too much nesting goin on here
					else if(temp.firstChild.firstChild!=null)
					{
						temp=temp.firstChild;
						//startIndex=prefix.length();
						continue;
					}
					//if the prefix you find is smaller than the existing prefix at currentNode with children
					else if(temp.firstChild!=null && prefix.length()<(temp.substr.endIndex-temp.substr.startIndex+1))
					{
						//fix temp
						temp.substr.startIndex=(short)(temp.substr.startIndex+1);
						//change the first child of the root to the new shorter prefix whos child is the prefix after that one
						root.firstChild=new TrieNode(new Indexes(words.indexOf(currentNode),(short)(startIndex),(short)(startIndex+prefix.length()-1)),temp,null);
						temp.sibling=new TrieNode(new Indexes(words.indexOf(word),(short)(startIndex+prefix.length()),(short)(word.length()-1)),null,null);
						break;
					}
					//if the prefix you find is LARGER than the existing prefix at currentNode with children
					else if(temp.firstChild!=null && prefix.length()>(temp.substr.endIndex-temp.substr.startIndex+1))
					{
						temp.firstChild.substr.endIndex=(short)(temp.substr.startIndex+prefix.length()-1);
						temp.firstChild.firstChild=new TrieNode(new Indexes(words.indexOf(currentNode),(short)(temp.substr.startIndex+prefix.length()),(short)(currentNode.length()-1)),null,null);
						temp.firstChild.firstChild.sibling=new TrieNode(new Indexes(words.indexOf(word),(short)(temp.substr.startIndex+prefix.length()),(short)(word.length()-1)),null,null);
						break;
					}
					//what i had before
					else
					{
						startIndex=startIndex+prefix.length();
						temp=temp.firstChild;
						continue;
					}
					
				}
				//if no common prefix is found between the word and the current word in the tree
				else
				{
					if(temp.sibling==null)
					{
						temp.sibling=new TrieNode(new Indexes(words.indexOf(word),(short)(temp.substr.startIndex),(short)(word.length()-1)),null,null);
						break;
					}
					else
						temp=temp.sibling;
					
				}
					
			}
			
			
		}
		
		
 	}
	
	/**
	 * Given a string prefix, returns its "completion list", i.e. all the words in the trie
	 * that start with this prefix. For instance, if the tree had the words bear, bull, stock, and bell,
	 * the completion list for prefix "b" would be bear, bull, and bell; for prefix "be" would be
	 * bear and bell; and for prefix "bell" would be bell. (The last example shows that a prefix can be
	 * an entire word.) The order of returned words DOES NOT MATTER. So, if the list contains bear and
	 * bell, the returned list can be either [bear,bell] or [bell,bear]
	 * 
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all words in tree that start with the prefix, order of words in list does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public ArrayList<String> completionList(String prefix) {
		/** COMPLETE THIS METHOD **/
		TrieNode temp=root.firstChild;
		ArrayList<String> list = new ArrayList<String>();
		while(temp!=null)
		{
			//finds a match
			String match="";
			String currentNode=words.get(temp.substr.wordIndex);
			for(int i=0;i<prefix.length();i++)
			{
				if(prefix.charAt(i)==currentNode.charAt(i))
				{
						match+=prefix.charAt(i);
				}
			else
				break;
			}
			
				if(match.length()>0)
				{
					if(temp.firstChild!=null)
					{
						if(temp.sibling!=null)
						{
							ArrayList<String> morewords=completionListHelper(prefix,temp.sibling);
							list.addAll(morewords);
						}
						temp=temp.firstChild;
						continue;
					}
					else
					{
						if(match.equals(prefix))
						{
						list.add(currentNode);
						}
						if(temp.sibling!=null)
						{
							temp=temp.sibling;
						}
						else
							break;
					}
				}
				else
				{
					temp=temp.sibling;
				}


			}
		
		
		
		/** FOLLOWING LINE IS A PLACEHOLDER FOR COMPILATION **/
		/** REPLACE WITH YOUR IMPLEMENTATION **/
		return list;
	}
	//recursive helper for 
	private ArrayList<String> completionListHelper(String prefix, TrieNode temp)
	{
		ArrayList<String> list = new ArrayList<String>();
		while(temp!=null)
		{
			//finds a match
			String match="";
			String currentNode=words.get(temp.substr.wordIndex);
			for(int i=0;i<prefix.length();i++)
			{
				if(prefix.charAt(i)==currentNode.charAt(i))
				{
						match+=prefix.charAt(i);
				}
			else
				break;
			}
			
				if(match.length()>0)
				{
					if(temp.firstChild!=null)
					{
						if(temp.sibling!=null)
						{
							ArrayList<String> morewords=completionListHelper(prefix,temp.sibling);
							list.addAll(morewords);
						}
						temp=temp.firstChild;
						continue;
					}
					else
					{
						if(match.equals(prefix))
						{
						list.add(currentNode);
						}
						if(temp.sibling!=null)
						{
							temp=temp.sibling;
						}
						else
							break;
					}
				}
				else
				{
					temp=temp.sibling;
				}


			}
		
		
		
		/** FOLLOWING LINE IS A PLACEHOLDER FOR COMPILATION **/
		/** REPLACE WITH YOUR IMPLEMENTATION **/
		return list;
	 
	
	}
	
	
	public void print() {
		print(root, 1, words);
	}
	
	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
