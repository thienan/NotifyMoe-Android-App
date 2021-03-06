package studio.lunabee.arn.ui.anime

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.default_error_view.*
import kotlinx.android.synthetic.main.fragment_animedetail.*
import studio.lunabee.arn.R
import studio.lunabee.arn.common.observeK
import studio.lunabee.arn.di.Injectable
import studio.lunabee.arn.ui.common.statefulview.Data
import studio.lunabee.arn.vo.Resource
import javax.inject.Inject

class AnimeDetailFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var animeDetailViewModel: AnimeDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_animedetail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animeDetailViewModel = ViewModelProviders.of(this,
            viewModelFactory).get(AnimeDetailViewModel::class.java)

        val userId = AnimeDetailFragmentArgs.fromBundle(arguments).userId

        animeDetailViewModel.setAnimeId(userId)
        animeDetailViewModel.anime.observeK(this) { userResource ->
            when (userResource) {
                is Resource.Loading -> statefulView.state = statefulView.loadingState
                is Resource.Failure -> {
                    subtitle.text = userResource.msg
                    statefulView.state = statefulView.errorState
                }
                is Resource.Success -> {
                    userResource.data?.let {
                        statefulView.state = Data()
                        animeTitleTextView.text = it.title?.canonical
                    }
                }
            }
        }
    }
}
