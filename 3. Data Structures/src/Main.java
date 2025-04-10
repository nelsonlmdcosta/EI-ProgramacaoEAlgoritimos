// Imports are includes like any other language, it allows you to access code/structures defined in another file/package
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("// Arrays and ArrayLists");
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");

        // Big O Notation https://www.w3schools.com/dsa/dsa_intro.php
        // Arrays are static sized objects that hold a specific number of objects inside it. No more no less
        // Arrays should generally be the prefered method of storing data as they are lightning fast. And CPU Cache Friendly!
        String[] Names = new String[]{ "Nelson", "Bob", "Marcelo", "Patricio", "Angela", "Flismina" };

        // System.out.println(Names[5]);
        // (Apontador A Memoria) + (Tamanho da memoria * indicie)
        // 0x000023412F

        // We Can Iterate Over Any Element And Print It Out
        System.out.println("Printing Names");
        for(int i = 0; i < Names.length; ++i)
        {
            System.out.println(Names[i]);
        }

        // We Cannot Insert But We Can Modify A Specific Element
        Names[1] = "Jose";
        System.out.println("\nPrinting Names After Modification");
        for(int i = 0; i < Names.length; ++i)
        {
            System.out.println(Names[i]);
        }

        // ArrayLists (Or Lists In Other Langauges) are Dynamic arrays.
        // These can grow their length, and resize as required. They are pretty efficient too, the only problem is the
        // constant reallocation of resources when growing the structure
        // NOTE: This does mean that you need to constantly deallocate and reallocate memory, which is why
        //       Under normal circumstances you want to reserve the amount of memory you're expecting to use
        // NOTE: asList returns a fixed size list and settign it to the list which means we cant change it
        List<String> NamesInArrayToListConversion = Arrays.asList(Names);
        // NOTE: We can however change this one as it's a clean construction
        List<String> NamesAsArrayListButEmpty = new ArrayList<String>( Arrays.asList(Names) );


        // You can iterate over these like any other Array
        System.out.println("\nPrinting Names In List");
        for(int i = 0; i < NamesAsArrayListButEmpty.size(); ++i)
        {
            System.out.println(NamesAsArrayListButEmpty.get(i));
        }

        // You can dynamically insert or remove from the structure
        NamesAsArrayListButEmpty.add("Josezocas");
        System.out.println("\nPrinting Names After Addition");
        for(int i = 0; i < NamesAsArrayListButEmpty.size(); ++i)
        {
            System.out.println(NamesAsArrayListButEmpty.get(i));
        }

        // You can dynamically insert or remove from the structure

        String TempToMove = NamesAsArrayListButEmpty.get(0);
        NamesAsArrayListButEmpty.set(0, NamesAsArrayListButEmpty.get(NamesAsArrayListButEmpty.size() - 1));
        NamesAsArrayListButEmpty.set(NamesAsArrayListButEmpty.size() - 1, null);


        //NamesAsArrayListButEmpty.remove("Nelson");
        System.out.println("\nPrinting Names After Removing");
        for(int i = 0; i < NamesAsArrayListButEmpty.size(); ++i)
        {
            System.out.println(NamesAsArrayListButEmpty.get(i));
        }

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("// Dictionary");
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");

        // Dictionarys are great when you want to associate a "Key" to a "Value" and you have a lot of elements
        // An Example could be associating a Name to an Age as shown below
        // Note that Dictionary is an abstract class so we can set the Hashtable which is derived into it
        // https://www.geeksforgeeks.org/java-util-dictionary-class-java/
        Dictionary<String, Integer> DictionaryExample = new Hashtable<String, Integer>();

        // Adding key-value pairs
        DictionaryExample.put("Adam", 25);
        DictionaryExample.put("Ben", 30);
        DictionaryExample.put("Catherine", 35);

        // Retrieving a value using a key
        System.out.println("Value of Ben: " + DictionaryExample.get("Ben"));

        // Replacing an existing value
        int oldValue = DictionaryExample.put("Catherine", 40);
        System.out.println("Old Value of Catherine: " + oldValue);

        // Removing a key-value pair
        DictionaryExample.remove("Adam");
        System.out.println("Removed Adam");

        // Displaying remaining key-value pairs
        Enumeration<String> EnumerationOrIterator = DictionaryExample.keys();
        while (EnumerationOrIterator.hasMoreElements())
        {
            // Find the key then use it!
            String key = EnumerationOrIterator.nextElement();
            System.out.println("Key: " + key + ", Value: " + DictionaryExample.get(key));
        }

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("// Stacks (FILO) and Queues (FIFO) ");
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");

        // https://www.geeksforgeeks.org/stack-class-in-java/
        Stack<Integer> StackExample = new Stack<>();

        // Use Push to place items at the back of the top of the stack
        StackExample.push(1);
        StackExample.push(2);
        StackExample.push(3);
        StackExample.push(4);
        StackExample.push(5);

        // Pop elements from the stack
        while(!StackExample.isEmpty())
        {
            // Use The Pop To Remove The Item, Pop Returns The Object
            System.out.println(StackExample.pop());
        }

        // Queues are basically LinkedLists with rules, so we use the Queue interface to ensure that's the rules
        Queue<Integer> QueueExample = new LinkedList<Integer>();

        QueueExample.add(1);
        QueueExample.add(2);
        QueueExample.add(3);
        QueueExample.add(4);
        QueueExample.add(5);

        // Pop elements from the stack
        while(!QueueExample.isEmpty())
        {
            // Use The Pop To Remove The Item, Pop Returns The Object
            System.out.println(QueueExample.remove());
        }

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("// Trees");
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("// Graphs");
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////");

        // https://www.geeksforgeeks.org/introduction-to-tree-data-structure/
    }
}