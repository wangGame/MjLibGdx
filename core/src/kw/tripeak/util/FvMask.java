package kw.tripeak.util;

public class FvMask {
//    boolean HasAny(const TFlag flag, const TMask mask) {
//        return ((flag & mask) != 0);
//    }
//
//    template<typename TFlag, typename TMask>
//    bool HasAll(const TFlag flag, const TMask mask) {
//        return ((flag & mask) == mask);
//    }
//
//    template<typename TFlag, typename TMask>
//    void Add(TFlag &flag, const TMask mask) {
//        flag |= mask;
//    }
//
//    template<typename TFlag, typename TMask>
//    void Del(TFlag &flag, const TMask mask) {
//        flag &= ~mask;
//    }
//
//    template<typename TFlag, typename TMask>
//    TFlag Remove(const TFlag flag, const TMask mask) {
//        return (flag & (~mask));
//    }
//
//    template<typename TFlag, typename TMask>
//    TFlag IsAdd(const TFlag oldFlag, const TFlag newFlag, const TMask mask) {
//        return (((oldFlag & mask) == 0) && ((newFlag & mask) != 0));
//    }
//
//    TFlag IsDel(const TFlag oldFlag, const TFlag newFlag, const TMask mask) {
//        return (((oldFlag & mask) != 0) && ((newFlag & mask) == 0));
//    }

    public static int _MASK_(int v) {
        return 0X01 << (v);
    }

    public static int Add(int m_cbTargetUser, int mask_) {
        return m_cbTargetUser |= mask_;
    }

    public static int Del(int flag, int mask) {
        return flag &= ~mask;
    }

    public static boolean HasAny(int m_cbTargetUser, int mask) {
        return ((m_cbTargetUser & mask) == mask);
    }
}
