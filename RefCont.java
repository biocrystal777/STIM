import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class RefCont{
    private CopyOnWriteArrayList <Substrate> AllSubs;
    private CopyOnWriteArrayList <Enzyme> AllEnzs;

    // setter & getter
    public CopyOnWriteArrayList <Substrate> get_AllSubs()
    {   return AllSubs;
    }
    public void set_AllSubs(CopyOnWriteArrayList <Substrate> dummy)
    {   AllSubs = dummy;
    }
    public CopyOnWriteArrayList <Enzyme> get_AllEnzs()
    {   return AllEnzs;
    }
    public void set_AllEnzs(CopyOnWriteArrayList <Enzyme> dummy)
    {   AllEnzs = dummy;
    }
}