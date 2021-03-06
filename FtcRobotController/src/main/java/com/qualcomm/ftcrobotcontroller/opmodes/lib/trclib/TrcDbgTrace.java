package com.qualcomm.ftcrobotcontroller.opmodes.lib.trclib;

import com.qualcomm.ftcrobotcontroller.opmodes.lib.hallib.HalDbgLog;
import com.qualcomm.ftcrobotcontroller.opmodes.lib.hallib.HalUtil;

public class TrcDbgTrace
{
    public static enum TraceLevel
    {
        QUIET(0),
        INIT(1),
        API(2),
        CALLBK(3),
        EVENT(4),
        FUNC(5),
        TASK(6),
        UTIL(7),
        HIFREQ(8);

        private int value;

        TraceLevel(int value)
        {
            this.value = value;
        }   //TraceLevel

        public int getValue()
        {
            return this.value;
        }   //getValue

    }   //enum TraceLevel

    public static enum MsgLevel
    {
        FATAL(1),
        ERR(2),
        WARN(3),
        INFO(4),
        VERBOSE(5);

        private int value;

        MsgLevel(int value)
        {
            this.value = value;
        }   //MsgLevel

        public int getValue()
        {
            return this.value;
        }   //getValue

    }   //enum MsgLevel

    private static int indentLevel = 0;

    private String instanceName;
    private boolean traceEnabled;
    private TraceLevel traceLevel;
    private MsgLevel msgLevel;
    private double nextTraceTime;

    public void setDbgTraceConfig(
            boolean traceEnabled,
            TraceLevel traceLevel,
            MsgLevel msgLevel)
    {
        this.traceEnabled = traceEnabled;
        this.traceLevel = traceLevel;
        this.msgLevel = msgLevel;
    }   //setDbgTraceConfig

    public TrcDbgTrace(
            final String instanceName,
            boolean traceEnabled,
            TraceLevel traceLevel,
            MsgLevel msgLevel)
    {
        this.instanceName = instanceName;
        setDbgTraceConfig(traceEnabled, traceLevel, msgLevel);
        this.nextTraceTime = HalUtil.getCurrentTime();
    }   //TrcDbgTrace

    public void traceEnter(
            final String funcName,
            final TraceLevel funcLevel,
            final String format,
            Object... args)
    {
        if (traceEnabled &&
            funcLevel.getValue() <= traceLevel.getValue())
        {
            HalDbgLog.traceMsg(
                    tracePrefix(funcName, true, false) + String.format(format, args) + ")\n");
        }
    }   //traceEnter

    public void traceEnter(
            final String funcName,
            final TraceLevel funcLevel)
    {
        if (traceEnabled &&
            funcLevel.getValue() <= traceLevel.getValue())
        {
            HalDbgLog.traceMsg(tracePrefix(funcName, true, true));
        }
    }   //traceEnter

    public void traceExit(
            final String funcName,
            final TraceLevel funcLevel,
            final String format,
            Object... args)
    {
        if (traceEnabled &&
            funcLevel.getValue() <= traceLevel.getValue())
        {
            HalDbgLog.traceMsg(
                    tracePrefix(funcName, false, false) + String.format(format, args) + "\n");
        }
    }   //traceExitMsg

    public void traceExit(
            final String funcName,
            final TraceLevel funcLevel)
    {
        if (traceEnabled &&
            funcLevel.getValue() <= traceLevel.getValue())
        {
            HalDbgLog.traceMsg(tracePrefix(funcName, false, true));
        }
    }   //traceExit

    public void traceFatal(
            final String funcName,
            final String format,
            Object... args)
    {
        traceMsg(funcName, MsgLevel.FATAL, 0.0, format, args);
    }   //traceFatal

    public void traceErr(
            final String funcName,
            final String format,
            Object... args)
    {
        traceMsg(funcName, MsgLevel.ERR, 0.0, format, args);
    }   //traceErr

    public void traceWarn(
            final String funcName,
            final String format,
            Object... args)
    {
        traceMsg(funcName, MsgLevel.WARN, 0.0, format, args);
    }   //traceWarn

    public void traceInfo(
            final String funcName,
            final String format,
            Object... args)
    {
        traceMsg(funcName, MsgLevel.INFO, 0.0, format, args);
    }   //traceInfo

    public void traceVerbose(
            final String funcName,
            final String format,
            Object... args)
    {
        traceMsg(funcName, MsgLevel.VERBOSE, 0.0, format, args);
    }   //traceVerbose

    public void tracePeriodic(
            final String funcName,
            double traceInterval,
            final String format,
            Object... args)
    {
        traceMsg(funcName, MsgLevel.INFO, traceInterval, format, args);
    }   //tracePeriodic

    private void traceMsg(
            final String funcName,
            MsgLevel level,
            double traceInterval,
            final String format,
            Object... args)
    {
        if (level.getValue() <= msgLevel.getValue())
        {
            double currTime = HalUtil.getCurrentTime();
            if (currTime >= nextTraceTime)
            {
                nextTraceTime = currTime + traceInterval;
                HalDbgLog.msg(level,
                              msgPrefix(funcName, level) + String.format(format, args) + "\n");
            }
        }
    }   //traceMsg

    private String tracePrefix(
            final String funcName,
            boolean enter,
            boolean newline)
    {
        String prefix = "";

        if (enter)
        {
            indentLevel++;
        }

        for (int i = 0; i < indentLevel; i++)
        {
            prefix += "| ";
        }

        prefix += instanceName + "." + funcName;

        if (enter)
        {
            prefix += newline? "()\n": "(";
        }
        else
        {
            prefix += newline? "!\n": "";
            indentLevel--;
        }

        return prefix;
    }   //tracePrefix

    private String msgPrefix(
            final String funcName,
            MsgLevel level)
    {
        String prefix = instanceName + "." + funcName;

        switch (level)
        {
        case FATAL:
            prefix += "_Fatal: ";
            break;

        case ERR:
            prefix += "_Err: ";
            break;

        case WARN:
            prefix += "_Warn: ";
            break;

        case INFO:
            prefix += "_Info: ";
            break;

        case VERBOSE:
            prefix += "_Verbose: ";
            break;

        default:
            prefix += "_Unk: ";
            break;
        }

        return prefix;
    }   //msgPrefix

}   //class TrcDbgTrace
