#pragma once
#ifndef STEAMBOT_STEAMBOT_H
#define STEAMBOT_STEAMBOT_H

#include "polyhook2/CapstoneDisassembler.hpp"
#include "polyhook2/Detour/x64Detour.hpp"
#include "polyhook2/Exceptions/BreakPointHook.hpp"
#include "../common.h"

namespace steambot {
    namespace module {
        inline uint64_t base;
        void load();
    }

    namespace globals {
        void load();
    }

    namespace functions {
        typedef void (*worldToScreen_t)(void*, void*, int, int, int);
        void load();
    }

    namespace hooks {
        void load();
    }

    void init();
}


#endif //STEAMBOT_STEAMBOT_H
