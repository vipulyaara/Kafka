package com.airtel.kafka

import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.entities.Item
import com.airtel.data.entities.ItemDetail
import com.airtel.data.model.data.Resource
import com.airtel.kafka.feature.content.BooksViewModel
import com.airtel.kafka.feature.content.ItemDetailViewModel
import com.airtel.kafka.feature.content.ItemRailViewModel
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 02/01/19.
 */
internal object BookService {
    private val booksViewModel: BooksViewModel by kodeinInstance.instance()
    private val itemDetailViewModel: ItemDetailViewModel by kodeinInstance.instance()
    private val itemRailViewModel: ItemRailViewModel by kodeinInstance.instance()

    //TODO
    fun getItemDetail(itemId: String): ServiceRequest<Resource<ItemDetail>> {
        return ServiceRequest { itemDetailViewModel.getItemDetail(itemId) }
    }

    fun getBooksByAuthor(author: String): ServiceRequest<Resource<List<Item>>> {
        return ServiceRequest { itemRailViewModel.getItemsByAuthor(author) }
    }
}
