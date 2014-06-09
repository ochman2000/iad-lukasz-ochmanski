package pl.lodz.p.iad.structure;
//import javax.annotation.NonNull;
//import javax.validation.constraints.NotNull;
import org.eclipse.jdt.annotation.*;

/**
 * A class representing a separator string, which can be used for separating
 * elements of a sequence while printing it, without special case treatment of
 * the first or last element. For example, the following program prints a list
 * of its arguments separated by commas, without using any conditionals.

   static void main(String[] args) {
   	Separator s = new Separator(", ");
   	for (String a : args)
   		System.out.print(s + a);
   }

 * Author: Yossi Gil (
 * See:  12/02/2006)
 */
public final class Separator {
    private final String s;
    boolean first = true;
    
    public Separator(final char c) {
        s = "" + c;
    }
    
    public Separator(final String s) {
        this.s = s;
    }
    
    @Override public String toString() {
        if (!first)
            return s;
        first = false;
        return "";
    }
    
    public static <T> boolean isEmpty(final Iterable<T> items) {
        return !items.iterator().hasNext();
    }
    
    public static <T> String wrap(final String wrap, final Iterable<T> items, final String between) {
        return wrap(wrap, wrap, items, between);
    }
    
    public static <T> String wrap(final String begin, final String end, final Iterable<T> items, final String between) {
        if (isEmpty(items))
            return "";
        String $ = begin;
        final Separator s = new Separator(between);
        for (final T t : items)
            $ += s + t.toString();
        return $ + end;
    }
    
    public static <T> String wrap(@NonNull final String begin, @NonNull final String end, @NonNull final T[] items,
            final String between) {
        if (items.length == 0)
            return "";
        String $ = begin;
        final Separator s = new Separator(between);
        for (final T t : items)
            $ += s + t.toString();
        return $ + end;
    }
    
    static void main(final String[] args) {
        final Separator s = new Separator(", ");
        for (final String a : args)
            System.out.print(s + a);
    }
}