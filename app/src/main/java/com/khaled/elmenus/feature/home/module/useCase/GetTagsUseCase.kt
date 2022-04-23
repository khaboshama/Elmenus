package com.khaled.elmenus.feature.home.module.useCase

import com.khaled.elmenus.common.BaseUseCase
import com.khaled.elmenus.feature.home.module.data.IHomeRepository

class GetTagsUseCase(repository: IHomeRepository) : BaseUseCase<IHomeRepository>(repository) {
    suspend operator fun invoke(pageNumber: Int) = repository.getTags(pageNumber)
}
