package com.converter.server.interfaces;

import com.converter.server.constants.PlatformTypes;

public interface ISearchable {

    PlatformTypes getTargetPlatform();

    String getSearchString();
}
