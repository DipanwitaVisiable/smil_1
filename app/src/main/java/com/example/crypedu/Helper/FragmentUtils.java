package com.example.crypedu.Helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * Created by INDID on 09-01-2018.
 */

public class FragmentUtils {

    /**
     * if there is a fragment and the back stack of this fragment is not empty,
     * then emulate 'onBackPressed' behaviour, because in default, it is not working.
     *
     * @param fm the fragment manager to which we will try to dispatch the back pressed event.
     * @return {@code true} if the onBackPressed event was consumed by a child fragment, otherwise
     */
    private static boolean dispatchOnBackPressedToFragments(FragmentManager fm) {

        List<Fragment> fragments = fm.getFragments();
        boolean result;
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment frag : fragments) {
                if (frag != null && frag.isAdded()) {
                    // go to the next level of child fragments.
                    result = dispatchOnBackPressedToFragments(frag.getChildFragmentManager());
                    if (result) return true;
                }
            }
        }

        // if the back stack is not empty then we pop the last transaction.
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            fm.executePendingTransactions();
            return true;
        }

        return false;
    }
}
