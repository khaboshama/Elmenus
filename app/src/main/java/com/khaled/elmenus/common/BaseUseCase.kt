package com.khaled.elmenus.common

import com.khaled.elmenus.common.data.IBaseRepository

abstract class BaseUseCase<Repository : IBaseRepository>(val repository: Repository)