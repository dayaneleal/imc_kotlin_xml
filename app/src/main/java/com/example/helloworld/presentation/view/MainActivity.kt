package com.example.helloworld.presentation.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.ValueAnimator
import androidx.core.content.ContextCompat
import com.example.helloworld.R
import com.example.helloworld.databinding.ActivityMainBinding
import com.example.helloworld.presentation.state.ImcUiState
import com.example.helloworld.presentation.model.InputError
import com.example.helloworld.presentation.viewModel.ImcViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ImcViewModel by viewModels()

    private val initialColor by lazy {
        ContextCompat.getColor(this, R.color.imc_yellow_overweight)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buildColoredBar()
        setupListeners()
        setupObservers()

        (binding.imcIndicator.background as? GradientDrawable)?.setColor(initialColor)
    }

    private fun setupListeners() {
        binding.btnCalculate.setOnClickListener {
            val weight = binding.weight.text.toString()
            val height = binding.height.text.toString()

            viewModel.calculateImc(weight, height)
        }

        binding.btnClear.setOnClickListener {
            viewModel.resetState()
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            if (state == null) {
                resetUiToInitialState()
            } else {
                updateUi(state)
            }
        }


        viewModel.validationError.observe(this) { event ->
            event.getContentIfNotHandled()?.let { state ->

                binding.weight.error = null
                binding.height.error = null

                state.weightError?.let { error ->
                    binding.weight.error = when (error) {
                        is InputError.Invalid -> getString(R.string.invalid_error)
                        is InputError.NotPositive -> getString(R.string.not_positive_error)
                    }
                }

                state.heightError?.let { error ->
                    binding.height.error = when (error) {
                        is InputError.Invalid -> getString(R.string.invalid_error)
                        is InputError.NotPositive -> getString(R.string.not_positive_error)
                    }
                }
            }
        }
    }

    private fun updateUi(uiState: ImcUiState) {
        binding.weight.error = null
        binding.height.error = null

        animateIndicator(uiState.indicatorPosition)

        val color = ContextCompat.getColor(this,uiState.classificationColorRes)
        val classificationText = getText(uiState.classificationTextRes)

        (binding.imcIndicator.background as? GradientDrawable)?.setColor(color)

        binding.tvImcValue.text = getString(R.string.imc_value_format, uiState.imc)
        binding.tvImcValue.setTextColor(color)

        binding.tvImcMessage.text = classificationText
        binding.tvImcMessage.setTextColor(color)
    }

    private fun resetUiToInitialState() {
        binding.weight.error = null
        binding.weight.text?.clear()
        binding.height.error = null
        binding.height.text?.clear()

        binding.tvImcValue.text = getString(R.string.imc_no_value)
        binding.tvImcValue.setTextColor(Color.BLACK)
        binding.tvImcMessage.text = getString(R.string.imc_message)
        binding.tvImcMessage.setTextColor(Color.BLACK)

        val constraintLayout = binding.cardConstraintLayout
        val set = ConstraintSet()
        set.clone(constraintLayout)
        set.setHorizontalBias(binding.imcIndicator.id, 0.5f)
        set.applyTo(constraintLayout)
        (binding.imcIndicator.background as? GradientDrawable)?.setColor(initialColor)
    }

    private fun buildColoredBar() {
        binding.imcBarContainer.removeAllViews()

        val barSegments = viewModel.getImcBarSegments()

        for (segment in barSegments) {
            val segmentView = LinearLayout(this).apply {
                setBackgroundColor(ContextCompat.getColor(this@MainActivity, segment.colorRes))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    segment.weight
                )
            }
            binding.imcBarContainer.addView(segmentView)
        }
    }

    private fun animateIndicator(finalPosition: Float) {
        val params = binding.imcIndicator.layoutParams as ConstraintLayout.LayoutParams
        val currentPosition = params.horizontalBias

        val animator = ValueAnimator.ofFloat(currentPosition, finalPosition)
        animator.duration = 700
        animator.addUpdateListener {
            val animatedPosition = animator.animatedValue as Float
            params.horizontalBias = animatedPosition
            binding.imcIndicator.layoutParams = params
        }
        animator.start()
    }
}