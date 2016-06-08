package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import latmod.lib.Bits;
import latmod.lib.IntList;
import latmod.lib.annotations.IFlagContainer;
import latmod.lib.annotations.IInfoContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.List;

public abstract class ConfigEntry implements IInfoContainer, IFlagContainer, IJsonSerializable
{
    private Byte flags;
    private String[] info;
    private ITextComponent displayName;

    ConfigEntry()
    {
    }

    public abstract ConfigEntryType getConfigType();

    @Override
    public abstract void fromJson(JsonElement o);

    @Override
    public abstract JsonElement getSerializableElement();

    public int getColor()
    {
        return 0x999999;
    }

    public String getDefValueString()
    {
        return null;
    }

    public String getMinValueString()
    {
        return null;
    }

    public String getMaxValueString()
    {
        return null;
    }

    public ITextComponent getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(ITextComponent c)
    {
        displayName = c;
    }

    public ConfigEntry copy()
    {
        ConfigEntry e = getConfigType().createNew();
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag, true);
        e.readFromNBT(tag, true);
        return e;
    }

    @Override
    public final String toString()
    {
        return getAsString();
    }

    public abstract String getAsString();

    public boolean getAsBoolean()
    {
        return false;
    }

    public int getAsInt()
    {
        return 0;
    }

    public double getAsDouble()
    {
        return 0D;
    }

    public IntList getAsIntList()
    {
        return new IntList(new int[] {getAsInt()});
    }

    public List<String> getAsStringList()
    {
        return Collections.singletonList(getAsString());
    }

    public ConfigGroup getAsGroup()
    {
        return null;
    }

    @Override
    public final void setFlag(byte flag, boolean b)
    {
        flags = Bits.setBit(flags == null ? 0 : flags, flag, b);

        if(flags == 0)
        {
            flags = null;
        }
    }

    @Override
    public final boolean getFlag(byte flag)
    {
        return flags != null && Bits.getBit(flags, flag);
    }

    @Override
    public final String[] getInfo()
    {
        return info;
    }

    @Override
    public final void setInfo(String[] s)
    {
        info = (s != null && s.length > 0) ? s : null;
    }

    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        if(extended)
        {
            if(flags != null)
            {
                tag.setByte("F", flags);
            }

            if(displayName != null)
            {
                tag.setString("N", ITextComponent.Serializer.componentToJson(displayName));
            }

            if(info != null && info.length > 0)
            {
                NBTTagList list = new NBTTagList();

                for(int i = 0; i < info.length; i++)
                {
                    list.appendTag(new NBTTagString(info[i]));
                }

                tag.setTag("I", list);
            }
        }
    }

    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        if(extended)
        {
            flags = tag.hasKey("F") ? tag.getByte("F") : null;

            displayName = tag.hasKey("N") ? ITextComponent.Serializer.fromJsonLenient(tag.getString("N")) : null;

            info = null;

            if(tag.hasKey("I"))
            {
                NBTTagList list = tag.getTagList("I", Constants.NBT.TAG_STRING);

                info = new String[list.tagCount()];

                for(int i = 0; i < info.length; i++)
                {
                    info[i] = list.getStringTagAt(i);
                }
            }
        }
    }

    public List<String> getVariants()
    {
        return null;
    }
}