package com.singularitycoder.materiallooksxml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.singularitycoder.materiallooksxml.databinding.ActivityMainBinding
import com.singularitycoder.materiallooksxml.databinding.ListItemComponentBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvMaterialComponents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = MaterialComponentAdapter(
                materialComponentList = Constants.materialComponentList,
                onComponentClick = { position: Int ->
                    val fragment = MaterialComponentDetailFragment(component = Constants.materialComponentList[position])
                    supportFragmentManager.beginTransaction().apply {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        add(binding.clMainActivityRoot.id, fragment)
                        addToBackStack(null)
                        commit()
                    }
                    println("Clicked ${Constants.materialComponentList[position]}")
                }
            )
        }
    }

    inner class MaterialComponentAdapter(
        private val materialComponentList: List<MaterialComponent>,
        private val onComponentClick: ((position: Int) -> Unit?)? = null
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding = ListItemComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MaterialComponentViewHolder(binding = binding)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as MaterialComponentViewHolder).bindItems(materialComponent = materialComponentList[position])
        }

        override fun getItemCount(): Int = materialComponentList.size

        override fun getItemViewType(position: Int): Int = position

        inner class MaterialComponentViewHolder(private val binding: ListItemComponentBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bindItems(materialComponent: MaterialComponent) {
                binding.apply {
                    ivImage.setImageResource(materialComponent.image)
                    tvTitle.text = materialComponent.title
                    tvSubTitle.text = materialComponent.subtitle
                    tvLink.visibility = View.GONE
                    onComponentClick ?: return
                    root.setOnClickListener { onComponentClick.invoke(adapterPosition) }
                }
            }
        }
    }
}