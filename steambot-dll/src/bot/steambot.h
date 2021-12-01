#pragma once
#ifndef STEAMBOT_STEAMBOT_H
#define STEAMBOT_STEAMBOT_H

#include "polyhook2/CapstoneDisassembler.hpp"
#include "polyhook2/Detour/x64Detour.hpp"
#include "polyhook2/Exceptions/BreakPointHook.hpp"
#include "../common.h"
#include "hooks/hooks.h"

namespace steambot {
    namespace module {
        inline uint64_t base;
        void load();
    }

    namespace functions {
        typedef void* (*doCycle_t)();
        void load();
    }

    namespace hooks {
        void load();
    }

    void init();
}


#endif //STEAMBOT_STEAMBOT_H
